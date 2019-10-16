package ie.gmit.ds;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.PasswordServiceGrpc.PasswordServiceImplBase;

public class PasswordServiceImpl extends PasswordServiceImplBase {
	
	@Override
	 public void hash(ie.gmit.ds.HashRequest request,
		        io.grpc.stub.StreamObserver<ie.gmit.ds.HashResponse> responseObserver) {
		 byte[] salt = Passwords.getNextSalt();  
		 byte[] hashed = Passwords.hash(request.getPassword().toCharArray(), salt);
		 
		 System.out.println(salt + " " + hashed);
		 HashResponse res = HashResponse.newBuilder().setHashed(ByteString.copyFrom(hashed))
		 	.setSalt(ByteString.copyFrom(salt)).setUserID(request.getUserID()).build();
		 try {
			 responseObserver.onNext(res);
		 }catch(RuntimeException e) {
			 System.out.println(e);
		 }
		 responseObserver.onCompleted();
	}
	
	@Override
	 public void validate(ie.gmit.ds.ValidateRequest request,
		        io.grpc.stub.StreamObserver<com.google.protobuf.BoolValue> responseObserver) {
		BoolValue valid = BoolValue.of(Passwords.isExpectedPassword(request.getPassword().toCharArray()
				, request.getSaltBytes().toByteArray(), request.getHashed().toByteArray()));
		
		try {
			responseObserver.onNext(valid);	
		}catch (RuntimeException e) {
			System.out.println(e);
		}
		
		responseObserver.onCompleted();
	}
}