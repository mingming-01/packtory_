package kr.co.itwillbs.de.common.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.common.vo.LoginVO;
import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.service.CommuteService;
import kr.co.itwillbs.de.groupware.dto.ApprovalDTO;
import kr.co.itwillbs.de.groupware.dto.NoticeDTO;
import kr.co.itwillbs.de.groupware.dto.NoticeSearchDTO;
import kr.co.itwillbs.de.groupware.service.ApprovalService;
import kr.co.itwillbs.de.groupware.service.NoticeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@Autowired
	private CommuteService commuteService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private ApprovalService approvalService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;

	private final String COMMON_MAJOR_CODE_TRADE = "WORK_STATUS_CODE";
	private final String CHECKIN_NAME_KR = "출근";
	private final String LATE_NAME_KR = "지각";
	
	/**
	 * 브라우저 주소창에 Host:port 만 입력 했을 때 들어오는 페이지 <br>
	 * 예시)localhost:8080 또는 localhost:8080/ 둘다 index.html 로 연결 시켜주기 위함 <br>
	 * ""만 있어도 가능하나, 고객사들이 "/" 남겨놓고 왜 안되냐는 VOC가 많아 PTSD적으로 작성하는 버릇이 있음
	 * 
	 * @return
	 */
	@GetMapping(value = { "", "/" })
	public String mainView() {
		LogUtil.logStart(log);

		return "/index";
	}

	/**
	 * 요즘도 이렇게 쓰나?
	 * <br> 이건 학원 서버에 올릴때 위에 있는 mainView의 @GetMapping 을 가져와서 패키지를 만들 용도
	 * <br> 301은 브라우져에게 "" 로 접속해서 리다이렉트 하는 주소("/login")가 임시 이동이 아니라 영구라는걸 알려준다. .. 이거 검색봇한테 주는 정보였나?
	 * @return
	 */
	public RedirectView main() {
		RedirectView redirectView = new RedirectView("/login");
		redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY); // 영구적 이동
		
		return redirectView;
	}
	
	/**
	 * 검색로봇에게 사이트 및 웹페이지를 수집할 수 있도록 허용하거나 제한하는 국제 권고안
	 * <br> robots.txt 에 대해서 (href: https://searchadvisor.naver.com/guide/seo-basic-robots )
	 * <br> (href: https://jihyunhillcs.tistory.com/39#Spring_Boot_%EC%97%90%EC%84%9C_%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0 )
	 * <br> 25.03.23 이 방식은 SpringMVC에서 쓰던 방법인데 Boot용으로 설정이 편리하다면 바꿀 예정
	 * 
	 * @return
	 */
	@GetMapping(value = { "/robots.txt", "/robot.txt" })
	@ResponseBody
	public String robots() {
		return "User-agent: *\n" + 
				"Allow: /\n" + 
				"Disallow: /files/*\n" + // 업로드한 파일 수집X
				"Disallow: /admin/*\n"; // 어드민 수집X
				// ERP나 MES인데.. 사실 전부 걸어야 하는거 아닌가?
	}

	@GetMapping("/test/draft")
	public String test() {
		return "groupware/approval/draft_form.html";
	}

	/**
	 * 로그인
	 * 
	 * @return
	 */
	@GetMapping(value = { "/login", "/login/" })
	public String loginForm(Model model) {
		LogUtil.logStart(log);

		model.addAttribute("loginVO", new LoginVO());

		return "main/login_form";
	}

	/**
	 * 로그인 > 성공 > 메인
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = { "/main", "/main/" })
	public String packtoryMainView(@ModelAttribute NoticeSearchDTO noticeSearchDTO, Model model) {
		LogUtil.logStart(log);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			LoginVO loginVO = userDetails.getLoginVO();
//			String memberId = userDetails.getUsername(); // 이렇게 자유롭게 사용 가능!
			LogUtil.logDetail(log,"userDetails is {}", userDetails);
			LogUtil.logDetail(log,"loginVO is {}", loginVO);
			model.addAttribute("userDetails", userDetails);
			model.addAttribute("loginVO", loginVO);
		}
		// =================================================================================
		// [ 공지사항 ]
		List<NoticeDTO> noticeList = noticeService.getNoticeList(noticeSearchDTO);
		model.addAttribute("noticeList", noticeList);
		
		
		// =================================================================================
		// [ 근태 ]
		// 출퇴근 기록 코드
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		model.addAttribute("codeItemList", codeItemList);
		
		// ------------------------------
		// 세션 아이디(사번) 불러오는 코드
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		String id = userDetails.getUsername();
		// ------------------------------
		
		// 로그인한 사번의 오늘 출퇴근 기록 조회
		LocalDate today = LocalDate.now();
		
		// 오늘 출근 기록
		CommuteDTO checkInRecord = commuteService.getCheckInTime(id, today);
		CommuteDTO checkOutRecord = commuteService.getCheckOutTime(id, today);
		
		// 마지막 기록
		CommuteDTO lastCommuteRecord = commuteService.getTodayLastCommute(id, today);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		model.addAttribute("checkInTime", checkInRecord != null ? checkInRecord.getRegDate().toLocalTime().format(formatter) : null);	// 오늘 출근기록 있을 경우 출근시간
		model.addAttribute("checkOutTime", checkOutRecord != null ? checkOutRecord.getRegDate().toLocalTime().format(formatter) : null);	// 오늘 퇴근기록 있을 경우 퇴근시간
		model.addAttribute("lastCommuteRecord", lastCommuteRecord);	// 오늘 출퇴근 마지막 기록

		// =================================================================================
		// [ 전자결재 ]
		List<ApprovalDTO> approvalList = approvalService.getApprovalListByFilter("waiting", id);
		log.info("approvalList : " + approvalList);
		model.addAttribute("approvalList", approvalList);
		
		return "/main/main";
	}
	
	// ----------------------------------------------------
	// 출퇴근 버튼 클릭 시 저장 ajax
	@PostMapping("/commute/save")	// "/commute/save"
	@ResponseBody
	public ResponseEntity<String> saveCommute(@RequestBody CommuteDTO commuteDTO) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log,"commuteDTO : {}", StringUtil.objToString(commuteDTO));
//		commuteDTO.setEmployeeId(id);	// 사번
		LocalDateTime now = LocalDateTime.now();
		commuteDTO.setRegDate(now); // 현재 시간 등록

		// 코드 리스트 조회 (WORK_STATUS_CODE)
		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		String checkInCode = getMinorCodeByMinorName(tradeCode, CHECKIN_NAME_KR); // "출근(1)"
		String lateCode = getMinorCodeByMinorName(tradeCode, LATE_NAME_KR);       // "지각(5)"
		
		
 		// 출근이면 지각 여부 확인
		if (checkInCode.equals(commuteDTO.getWorkStatusCode())) {
			LocalTime standardTime = LocalTime.of(9, 10); // 오전 9시 10분 기준
			if (now.toLocalTime().isAfter(standardTime)) {
				commuteDTO.setWorkStatusCode(lateCode); // 지각 처리
				log.info("※ 지각 처리됨!!!!!!!!!: {}", lateCode);
			}
		}
		
		// 출퇴근 기록 요청(insert)
		commuteService.saveCommuteInfo(commuteDTO);
		return ResponseEntity.ok("저장 완료");
	}
	
	// ===================================================================
	/**
	 * 특정 minorName을 가진 minorCode를 반환하는 유틸 메서드
	 * @param codeList
	 * @param targetName(한글명)
	 * @return
	 */
	private String getMinorCodeByMinorName(List<CodeItemDTO> codeList, String targetName) {
		for (CodeItemDTO item : codeList) {
			if (targetName.equals(item.getMinorName())) {
				return item.getMinorCode();
			}
		}
		return null;
	}
	
	@GetMapping("/main/sitemap")
	public String getSiteMap() {
		LogUtil.logStart(log);
		
		return "/main/sitemap";
	}
}
