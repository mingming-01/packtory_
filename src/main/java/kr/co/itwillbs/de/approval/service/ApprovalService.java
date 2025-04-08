package kr.co.itwillbs.de.approval.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.approval.dto.ApprovalDTO;
import kr.co.itwillbs.de.approval.dto.DraftDTO;
import kr.co.itwillbs.de.approval.mapper.ApprovalMapper;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalService {
	@Autowired
	private ApprovalMapper approvalMapper;
	@Autowired
	private CommonService commonService;

	
	//	=====================================================================================
	//	보라씨 작업
	//	------------------------------------------------------------
	// 로그인한 userId로 사원 정보 가져오기
	public DraftDTO getEmployeeInfo(String userId) {
		return approvalMapper.selectEmployeeInfo(userId);
	}
	
	//	------------------------------------------------------------
	//	기안서 등록(저장) 요청
	@LogExecution // 로그 남길 서비스
	public String registerApproval(DraftDTO draftDTO) {
		
		// ----------- approval_no 결재번호 생성 ----------- 
		// "A"+ yyyyMMdd + 4자리 숫자 (총13자리)  예) A202503270001
//		String approval_no = draftDTO.getApprovalNo();
		
//		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
//		int code = 1;
		
		// DB에서 오늘 날짜의 최대 결재번호 조회
//		String lastApprovalNo = approvalMapper.getLastApprovalNo(today);
//		System.out.println("최대 결재번호: " + lastApprovalNo); //A202503270001
		
//		if(lastApprovalNo != null) { // 기안한 것이 있을 경우 최대 결재번호의 마지막 4자리 숫자에서 +1
//			int lastCode = Integer.parseInt(lastApprovalNo.substring(9));
//			code = lastCode + 1;
//		}
		
//		String formattedCode = String.format("%04d", code); // 4자리 숫자로 포맷
//		approval_no = "A" + today + formattedCode; // 최종 결재번호 생성
		
//		String approval_no = commonService.getApprovalNoFromMySQL();
		//	------------------------------------------------------------
		//	approval_no 결재번호 생성
		String year = new SimpleDateFormat("yyyy").format(new Date());
		String approval_no = "A" + year + "-" + commonService.getApprovalNoFromMySQL();
		
		draftDTO.setApprovalNo(approval_no);
		
		//	서버로 넘어오는 값(ex - 2025-04-03 ~ 2025-04-10)
		//	~ 기준으로 뒤쪽 날짜를 dueDate(마감일자)로 세팅
		draftDTO.setDueDate(draftDTO.getDueDate().split("~")[1].trim());
		log.info("draftDTO : " + draftDTO);
		//DraftDTO(approval_no=A202503280001, drafter_id=100008, drafter_name=김보라, drafter_department=아이티윌, drafter_position=주임, 
		// approval_type=null, doc_no=, approver1=, approver2=, approver3=, title=ㅇㅇㄹㄴ, content=ㅇㄴㄹㅇ, due_date=null, approvalStatus=null)
		//	-------------------------------------------------------------
		//	기안서 저장
		approvalMapper.insertApproval(draftDTO); // DB 비즈니스 로직 처리
		
		//	생성한 결재번호 리턴
		return approval_no;
		
	}
	//	=====================================================================================
	
	//	------------------------------------------------------------
	//	결재서류 전체 목록 조회
	public List<ApprovalDTO> getApprovalList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return approvalMapper.getApprovalList();
	}

	//	=====================================================================================
	//	결재라인을 위한 모든 회원 목록 조회
	public List<DraftDTO> getAllEmployeeInfo() {
		return approvalMapper.getAllEmployeeInfo();
	}
	//	결재라인 AJAX로 검색어 조회
	public List<DraftDTO> getSearchEmployeeInfo(String keyword) {
		return approvalMapper.getSearchEmployeeInfo(keyword);
	}
	
	/**
	 * 결재번호로 전자결재문서 가져오기
	 * @param approvalNo
	 * @return ApprovalDTO
	 */
	public ApprovalDTO getApprovalByApprovalNo(String approvalNo) {
		return approvalMapper.getApprovalByApprovalNo(approvalNo);
	}

	
	
	
	
	
	
	
	
}
