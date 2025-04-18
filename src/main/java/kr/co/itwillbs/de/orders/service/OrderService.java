package kr.co.itwillbs.de.orders.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;
import kr.co.itwillbs.de.orders.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {
	
	private final OrderMapper orderMapper;
	private final CommonService commonService;
	
	public OrderService(OrderMapper orderMapper, CommonService commonService) {
		this.orderMapper = orderMapper;
		this.commonService = commonService;
	}
	
	/**
	 * 수주/발주/구매 정보 조건 카운트 가져오기 페이징용
	 * @param orderSearchDTO
	 * @return
	 */
	public int getOrderCountForPaging(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return orderMapper.getOrderCountForPaging(orderSearchDTO);
	}
	
	/**
	 * 수주/발주/구매 정보 조건 검색 가져오기
	 * @param orderSearchDTO
	 * @return List<OrderDTO>
	 */
	public List<OrderDTO> getOrderList(OrderSearchDTO orderSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return orderMapper.getOrderList(orderSearchDTO);
	}
	
	// ------------------------------------------------------------------------------------
	/**
	 * 상품정보 조건 검색 가져오기
	 * @param productSearchDTO
	 * @return List<ProductDTO>
	 */
	public List<ProductDTO> getItemList(ProductSearchDTO productSearchDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 수주/발주 주문 정보 등록(INSERT)
	 * <br> order, orderDetail, orderItems
	 * @param orderFormDTO
	 * @return 
	 */
	@LogExecution // 로그 남길 서비스
	public void registerOrder(OrderFormDTO orderFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// MySQL DB에서 시퀀스 가져와서 document_number에 넣기
		orderFormDTO.setDocumentNumber(commonService.getSeqOrderNumberfromMySQL());

		orderMapper.insertOrder(orderFormDTO);
		orderMapper.insertOrderDetail(orderFormDTO);
		orderMapper.insertOrderItems(orderFormDTO);
	}

	// ------------------------------------------------------------------------------------
	/**
	 * 수주/발주 상세 정보 가져오기
	 * @param documentNumber
	 * @return OrderDTO
	 */
	@Deprecated
	public OrderDTO oldgetOrderByDocumentNumber(String documentNumber) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return orderMapper.oldgetOrderByDocumentNumber(documentNumber);
	}
	
	/**
	 * 수주/발주 상세 정보 가져오기
	 * @param documentNumber
	 * @return OrderFormDTO
	 */
	public OrderFormDTO getOrderByDocumentNumber(String documentNumber) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		OrderFormDTO orderFormDTO = orderMapper.getOrderByDocumentNumber(documentNumber);		// order, orderDetail 정보
		orderFormDTO.setOrderItems(orderMapper.getOrderListByDocumentNumber(documentNumber));	// orderItems 정보
		
		return orderFormDTO;
	}

	// ------------------------------------------------------------------------------------
	/**
	 * 수주/발주 주문 정보 수정(UPDATE)
	 * @param orderDTO
	 * @param orderDetailDTO
	 * @return 
	 */
	@LogExecution // 로그 남길 서비스
	public void modifyOrder(OrderFormDTO orderFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		orderMapper.updateOrder(orderFormDTO);
		orderMapper.updateOrderDetail(orderFormDTO);
		
		// documentNumber 일치하는 아이템 delete 후 다시 insert
		// => 바뀐 값 하나하나 비교해서 update 하기 번거로움 ..
		orderMapper.deleteOrderItemsByDocumentNumber(orderFormDTO.getDocumentNumber());
		orderMapper.insertOrderItems(orderFormDTO);	// 재사용
	}

	
}
