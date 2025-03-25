package kr.co.itwillbs.de.orders.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/client")
	public String orderMain(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<ClientDTO> clientDTOList = orderService.getClientList();
		
		log.info("확인용 : " + clientDTOList);
		
		model.addAttribute("clientDTOList", clientDTOList);
		model.addAttribute("clientDTO", new ClientDTO());
		
		return "orders/client_main.html";
	}
	
	@PostMapping("/client")
	@ResponseBody
	public String insertClient(@ModelAttribute ClientDTO clientDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("input params : {}",clientDTO.toString());
//		orderService.insertClient(clientDTO);
		
		
		if(orderService.insertClient(clientDTO) > 0) {
			return "success";
		} else {
			//	TODO 에러페이지 생성 시 수정 필요
			return "fail";
		} 
		
	}
	
	@GetMapping("/clientDetail")
	public String clientDetail(@RequestParam(value = "idx") String idx, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
//		System.out.println(">>>>>" + idx);
		
		ClientDTO clientDTO = orderService.getClient(idx);
		
		model.addAttribute("clientDTO", clientDTO);
		
		
		return "orders/client_detail.html";
	}
	
	
	
}
