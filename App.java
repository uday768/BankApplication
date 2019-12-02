package com.comple.completable_future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		list.add(415);
		list.add(4151);
		list.add(4152);
		list.add(4153);
		list.add(1231);
		list.add(1232);
		list.add(1233);
		ExecutorService service = Executors.newFixedThreadPool(8);
		System.out.println(Thread.currentThread().getName());
		List<CompletableFuture<AccountTransaction[]>> futures = new ArrayList<CompletableFuture<AccountTransaction[]>>();
		for (Integer acc : list) {
			futures.add(CompletableFuture.supplyAsync(() -> getRes(acc), service));
		}
		service.shutdown();
		for(CompletableFuture<AccountTransaction[]> fut : futures) {
			try {
				System.out.println(fut.get()[0]);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static AccountTransaction[] getRes(Integer acc) {
		System.out.println(acc);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName());
		RestTemplate restCall = new RestTemplate();
		ResponseEntity<AccountTransaction[]> forEntity = restCall
				.getForEntity("http://localhost:8080/accounts/" + acc + "/transactions", AccountTransaction[].class);
		AccountTransaction[] body = forEntity.getBody();
		return body;
	}
}
