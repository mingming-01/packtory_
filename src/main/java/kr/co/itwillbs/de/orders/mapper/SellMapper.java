package kr.co.itwillbs.de.orders.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;

@Mapper
public interface SellMapper {

    /**
     * 수주/발주 정보 조건 검색 가져오기
     * SELECT FROM t_order inner join t_order_detail where orderSearchDTO
     * @param orderSearchDTO 리스트 화면 검색필드 DTO
     * @return List<OrderDTO>
     */
    List<OrderDTO> getOrderList(OrderSearchDTO orderSearchDTO);
    
    // -------------------------------------------------------------------
    /**
	 * 수주/발주 주문 정보 등록(INSERT) >> orderDTO
	 * @param orderDTO
	 * @return 
	 */
	void insertOrder(@Valid OrderDTO orderDTO);
	
	/**
	 * 수주/발주 주문 정보 등록(INSERT) >> orderDetailDTO
	 * @param orderDetailDTO
	 * @return 
	 */
	void insertOrderDetail(OrderDetailDTO orderDetailDTO);

	// -------------------------------------------------------------------
	/**
	 * SELECT FROM t_order inner join t_order_detail where documentNumber
	 * @param documentNumber
	 * @return OrderDTO
	 */
	OrderDTO getOrderByDocumentNumber(String documentNumber);
	
	// -------------------------------------------------------------------
	/**
	 * 수주/발주 주문 정보 수정(UPDATE) >> orderDTO
	 * @param orderDTO
	 * @return 
	 */
	void updateOrder(OrderDTO orderDTO);
	
	/**
	 * 수주/발주 주문 정보 수정(UPDATE) >> orderDetailDTO
	 * @param orderDetailDTO
	 * @return 
	 */
	void updateOrderDetail(OrderDetailDTO orderDetailDTO);

}
