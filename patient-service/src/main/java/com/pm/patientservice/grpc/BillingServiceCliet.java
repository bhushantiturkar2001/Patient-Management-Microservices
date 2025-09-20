package com.pm.patientservice.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class BillingServiceCliet {

	private final Logger log = LoggerFactory.getLogger(BillingServiceCliet.class);

	private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

	public BillingServiceCliet(@Value("${billing.service.address:localhost}") String serverAddress,
			@Value("${billing.service.grpc.port:9001}") int serverPort) {

		log.info("COnnecting to BIlling Service at {}:{}", serverAddress, serverPort);

		ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();

		blockingStub = BillingServiceGrpc.newBlockingStub(channel);
	}

	public BillingResponse createBillingAccount(String patientId, String name, String email) {
		BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId).setName(name).setEmail(email)
				.build();
		
		BillingResponse response = blockingStub.createBillingAccount(request);
		
		log.info("Recived response from billing service via grpc: {}",response);
		
		return response;
	}

}
