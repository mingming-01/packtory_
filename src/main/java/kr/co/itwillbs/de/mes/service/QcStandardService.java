package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardSearchDTO;
import kr.co.itwillbs.de.mes.mapper.QcStandardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QcStandardService {

	private final QcStandardMapper qcStandardMapper;

	// 품질 등록
	@LogExecution
	@Transactional
	public String insertQcStandard(QcStandardDTO qcStandardDTO) {
		log.info("품질 등록 요청: {}", qcStandardDTO);
		qcStandardMapper.insertQcStandard(qcStandardDTO);
		log.info("품질 등록 완료 - name: {}", qcStandardDTO.getName());
		return "redirect:/mes/warehouse";
	}

	// 품질 총 개수 (검색 조건 포함)
	public int searchQcStandardCount(QcStandardSearchDTO searchDTO) {
		log.info("품질 개수 조회 - 검색 조건: {}", searchDTO);
		return qcStandardMapper.searchQcStandardCount(searchDTO);
	}

	// 품질 목록 조회 (검색 + 페이징)
	public List<QcStandardDTO> searchQcStandard(QcStandardSearchDTO searchDTO) {
		log.info("품질 목록 조회 - 검색 조건: {}", searchDTO);
		return qcStandardMapper.searchQcStandard(searchDTO);
	}

	// 품질 상세 조회
	public QcStandardDTO getQcStandardByIdx(Long idx) {
		log.info("품질 상세 조회 - idx: {}", idx);
		return qcStandardMapper.getQcStandardByIdx(idx);
	}

	// 품질 수정
	@LogExecution
	@Transactional
	public void updateQcStandard(QcStandardDTO qcStandardDTO) {
		log.info("품질 수정 요청 - idx: {}", qcStandardDTO.getIdx());

		// productDTO가 null이 아닌지 체크
		if (qcStandardDTO != null) {
			// 품질 정보를 업데이트하는 쿼리 호출
			qcStandardMapper.updateQcStandard(qcStandardDTO);
			log.info("품질 수정 완료 - name: {}", qcStandardDTO.getName());
		}
	}

	// 품질 삭제 (Soft delete)
	@Transactional
	public void deleteQcStandard(Long idx) {
		log.info("품질 삭제 요청 - idx: {}", idx);
		qcStandardMapper.deleteQcStandard(idx);
	}

}