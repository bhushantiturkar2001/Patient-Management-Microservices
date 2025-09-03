package com.pm.billingservice.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
	
	private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

	@Override
	public void createBillingAccount(billing.BillingRequest billingRequest, 
			StreamObserver<BillingResponse> responseObserver) {
		
		log.info("createBillingAccount request recived {}",billingRequest.toString());
		
		// BL
		
		BillingResponse response = BillingResponse.newBuilder()
				.setAccountId("12345")
				.setStatus("ACTIVE")
				.build();
		
	   responseObserver.onNext(response); // Send res from GRPC service billing service back to the client patient  service
	   responseObserver.onCompleted(); // res completed end of cycle
	   
		
	}

}
