package kr.co.itwillbs.de.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.admin.dto.CodeDTO;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.admin.dto.CodeSearchDTO;
import kr.co.itwillbs.de.admin.service.CodeService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/code")
public class CodeController {

	private final CodeService codeService;
	private final CommonCodeUtil commonCodeUtil;
	//@Autowired
	public CodeController(CodeService codeService, CommonCodeUtil commonCodeUtil) {
		this.codeService = codeService;
		this.commonCodeUtil = commonCodeUtil;
	}

	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String CODE_PATH="/admin/code";
	
	/**
	 * 어드민 > 코드 관리 > 공통코드 등록
	 * @param model
	 * @return
	 */
	@GetMapping(value="/new")
	public String codeRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		model.addAttribute("codeDTO", new CodeDTO());
		
		return CODE_PATH+"/code_register_form";
	}
	
	/**
	 * 어드민 > 코드 관리 > 공통코드 등록(INSERT)
	 * @param logDTO
	 * @return
	 */
//	public String registerCode(@ModelAttribute("codeDTO") @Valid CodeDTO codeDTO,
//			 					BindingResult bindingResult, Model model) {
	@PostMapping(value={"","/"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE) // 이녀석 만고 필요 없...던데? 
	public String registerCode(@ModelAttribute("codeDTO") @Valid CodeDTO codeDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(codeDTO));
		
		if(codeService.registerCode(codeDTO) < 1) {
			return CODE_PATH+"/log_register_form";
		}
		return "redirect:"+CODE_PATH;
	}
	
	/**
	 * 어드민 > 코드 관리 > 코드리스트
	 * @param model
	 * @return
	 */
	@GetMapping(value={"","/"})
	public String getCodes(@ModelAttribute CodeSearchDTO codeSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 리스트 검색 DTO 뷰에 전달
		codeSearchDTO.getPageDTO().setTotalCount(codeService.getCodesCountBySearchDTO(codeSearchDTO));
		// 검색폼 공통코드 리스트 셋
		model.addAttribute("codeSearchDTO", this.setSearchDTOcodeList(codeSearchDTO));
		
		List<CodeDTO> codeDTOList = codeService.getCodesBySearchDTO(codeSearchDTO);
		model.addAttribute("codeDTOList", codeDTOList);
		
		return CODE_PATH+"/code_list";
	}
	
	/**
	 * 어드민 > 코드 관리 > 코드리스트 검색 조건 조회
	 * @param codeSearchDTO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/search")
	public String getCodesBySearchDTO(@ModelAttribute CodeSearchDTO codeSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("request codeSearchDTO : {}", StringUtil.objToString(codeSearchDTO));
		// 리스트 검색 DTO 뷰에 전달
		codeSearchDTO.getPageDTO().setTotalCount(codeService.getCodesCountBySearchDTO(codeSearchDTO));
		// 검색폼 공통코드 리스트 셋
		model.addAttribute("codeSearchDTO", this.setSearchDTOcodeList(codeSearchDTO));
		// 리스트 결과 DTOlist 뷰에 전달
		model.addAttribute("codeDTOList", codeService.getCodesBySearchDTO(codeSearchDTO));
		
		return CODE_PATH+"/code_list";
	}
	
	/**
	 * 어드민 > 코드 관리 > 코드 상세화면
	 * <br> 상세 화면에서 하위 코드 입력/수정도 함께 가능
	 * @param id t_commoncode.idx 값 
	 * @param model codeDTO(major), codeItemDTOList(minor)
	 * @return
	 */
	@GetMapping("/{idx}")
	public String getCode(@PathVariable("idx") String idx,
							@ModelAttribute CodeSearchDTO codeSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("request id : {}, codeSearchDTO : {}", idx, codeSearchDTO);
		
		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			model.addAttribute("codeSearchDTO", codeSearchDTO);
			return "redirect:"+CODE_PATH;
		}
		
		try {
			// t_commoncode 테이블 조회를 하기 위해 idx 셋
			codeSearchDTO.setIdx(idx);
			CodeDTO codeDTO = codeService.getCodeByIdx(codeSearchDTO);
			if(codeDTO != null) {
				// 공통코드 셋
				model.addAttribute("codeDTO", codeDTO);
				// 공통 코드 가져오기(TEST) codeDTO.getMajorCode()
//				model.addAttribute("codeItems", commonCodeUtil.getCodeItems(codeDTO.getMajorCode()));
				// 조회된 결과 값에서 하위 공통코드 조회를 위해 majorCode 셋
				codeSearchDTO.setMajorCode(codeDTO.getMajorCode());
				log.info("codeDTO.getMajorCode() {}", codeDTO.getMajorCode());
				// 하위코드 조회하기 근데 내가 왜 SearchDTO 에 담아서 보냈는지 이해가 되질 않네?
				model.addAttribute("codeItemDTOList", codeService.getCodeItemsByMajorCode(codeSearchDTO));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			model.addAttribute("codeSearchDTO", codeSearchDTO);
			return "redirect:"+CODE_PATH;
		}
		
		return CODE_PATH+"/code_detail";
	}
	
	/**
	 * 어드민 > 코드 관리 > 코드상세 > 그룹코드 수정
	 * @param codeDTO
	 * @param model
	 * @return
	 */
	@PutMapping("/modifyCode")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyCode(@RequestBody @Valid CodeDTO codeDTO,
															BindingResult bindingResult, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestBody : {}", codeDTO);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			codeService.modifyCode(codeDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}
		
		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아니었나? 하는 기억에 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 어드민 > 코드 관리 > 코드상세 > 하위코드 입력/수정
	 * <br> 전체 삭제 후 인서트 작업이기에 등록/수정 한곳에서 이루어짐
	 * @param itemList jsonObj >> {"majorCode" : "foo", "minorCode": "bar", "minorName": "foobar", "description": "st", "rankNumber":n, "isDeleted":"Y||N"}
	 * @param model
	 * @return
	 */
	@PostMapping("/registerCodeItems")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> registerCodeItems(@RequestBody List<CodeItemDTO> itemList,  Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestBody : {}", itemList);
		
		//리턴 객체
		Map<String, Object> response = new HashMap<>();
		try {
			codeService.registerCodeItems(itemList);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아닐까 하는 생각에 이렇게 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
	
	private CodeSearchDTO setSearchDTOcodeList(CodeSearchDTO codeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		codeSearchDTO.setCodeList(codeService.getCodes());
		return codeSearchDTO;
	}
	
}
