package kr.co.itwillbs.de.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.service.SearchService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.human.dto.EmployeeCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;
import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.ClientSearchDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;

	// 사원 조회 팝업창으로 이동
	@GetMapping(value = { "/employee/search-popup", "/employee/search-popup/" })
	public String getEmployeeList(Model model, @ModelAttribute EmployeeSearchDTO employeeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 사원 리스트 조회 요청(SELECT)
		List<EmployeeDTO> employeeList = searchService.getEmployeeList(employeeSearchDTO);
		model.addAttribute("employeeList", employeeList);
		
		//페이징용 totalCount
		employeeSearchDTO.getPageDTO().setTotalCount(searchService.getEmployeeCountForPaging(employeeSearchDTO));
		model.addAttribute("employeeSearchDTO", employeeSearchDTO);
		
		return "/common/employee_search_form";
	}

	// 검색조건에 따른 사원 목록
	@PostMapping("/employee/search-popup")
	@ResponseBody
	public List<EmployeeDTO> searchEmployeeList(Model model, @ModelAttribute EmployeeSearchDTO employeeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println(">>>>>>>>>>>>>>>>>>" + employeeSearchDTO);

		//페이징용 totalCount
		employeeSearchDTO.getPageDTO().setTotalCount(searchService.getEmployeeCountForPaging(employeeSearchDTO));
		model.addAttribute("employeeSearchDTO", employeeSearchDTO);
		
	    return searchService.getEmployeeList(employeeSearchDTO);
	}
	
	// ----------------------------------------------------------------------------------
	// 부서 리스트 조회
	@GetMapping("/api/getDepartments")
	@ResponseBody
	public List<EmployeeCodeDTO> getDepartmentList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchService.getDepartmentList(); // major_code 기준
	}
	
	// 하위 부서 리스트 조회
	@GetMapping("/api/getSubDepartments")
	@ResponseBody
	public List<EmployeeCodeDTO> getSubDepartmentList(@RequestParam("departmentCode") String departmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    return searchService.getSubDepartmentList(departmentCode); // 하위 minor_code
	}

	// 직급 리스트 조회
	@GetMapping("/api/getPositions")
	@ResponseBody
	public List<EmployeeCodeDTO> getPositionList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		return searchService.getPositionList(); // major_code 기준
	}
	
	// ==================================================================================
	// 거래처 조회 팝업창으로 이동
	@GetMapping(value = { "/client/search-popup", "/client/search-popup/" })
	public String getClientList(Model model, @ModelAttribute ClientSearchDTO clientSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 사원 리스트 조회 요청(SELECT)
		List<ClientDTO> clientList = searchService.getClientList(clientSearchDTO);
		model.addAttribute("clientList", clientList);
		
//		//페이징용 totalCount
		clientSearchDTO.getPageDTO().setTotalCount(searchService.getClientCountForPaging(clientSearchDTO));
		model.addAttribute("clientSearchDTO", clientSearchDTO);
		
		return "/common/client_search_form";
	}
	
	// 검색조건에 따른 거래처 목록
	@PostMapping("/client/search-popup")
	@ResponseBody
	public List<ClientDTO> searchClientList(Model model, @ModelAttribute ClientSearchDTO clientSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println(">>>>>>>>>>>>>>>>>>" + clientSearchDTO);
		
		//페이징용 totalCount
		clientSearchDTO.getPageDTO().setTotalCount(searchService.getClientCountForPaging(clientSearchDTO));
		model.addAttribute("clientSearchDTO", clientSearchDTO);
		
		return searchService.getClientList(clientSearchDTO);
	}

	// ==================================================================================
	// 상품 조회 팝업창으로 이동
	@GetMapping(value = { "/product/search-popup", "/product/search-popup/" })
	public String getProductList(Model model, @ModelAttribute ProductSearchDTO productSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 상품 리스트 조회 요청(SELECT)
		List<ProductDTO> productList = searchService.getProductList(productSearchDTO);
		model.addAttribute("productList", productList);
		
//		//페이징용 totalCount
		productSearchDTO.getPageDTO().setTotalCount(searchService.getProductCountForPaging(productSearchDTO));
		model.addAttribute("productSearchDTO", productSearchDTO);
		
		return "/common/product_search_form";
	}
	
	// 검색조건에 따른 거래처 목록
	@PostMapping("/product/search-popup")
	@ResponseBody
	public List<ProductDTO> searchProductList(Model model, @ModelAttribute ProductSearchDTO productSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println(">>>>>>>>>>>>>>>>>>" + productSearchDTO);
		
		//페이징용 totalCount
		productSearchDTO.getPageDTO().setTotalCount(searchService.getProductCountForPaging(productSearchDTO));
		model.addAttribute("productSearchDTO", productSearchDTO);
		
		return searchService.getProductList(productSearchDTO);
	}
	

}
