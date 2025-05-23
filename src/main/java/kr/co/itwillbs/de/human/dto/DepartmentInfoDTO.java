package kr.co.itwillbs.de.human.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import kr.co.itwillbs.de.human.entity.DepartmentInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = { "regId", "modId" })
public class DepartmentInfoDTO {

	private Long idx;

	@NotEmpty(message = "대표부서코드는 필수 입력 값입니다.")
	private String departmentCode;

	private String departmentName;

	@NotEmpty(message = "부모코드는 필수 입력 값입니다.")
	private String parentCode;

	@NotEmpty(message = "하위부서코드는 필수 입력 값입니다.")
	private String childCode;

	@NotEmpty(message = "하위부서이름은 필수 입력 값입니다.")
	private String childName;

	@NotEmpty(message = "정렬순서는 필수 입력 값입니다.")
	private String rankNumber;

	private String isDeleted;

	@NotEmpty(message = "장소참조는 필수 입력 값입니다.")
	private String locationIdx;

	private String regId;

	private LocalDateTime regDate;

	private String modId;

	private LocalDateTime modDate;

	@Builder
	public DepartmentInfoDTO(Long idx, String departmentCode, String parentCode, String childCode, String childName,
			String rankNumber, String isDeleted, String locationIdx, String regId, LocalDateTime regDate, String modId,
			LocalDateTime modDate) {
		this.idx = idx;
		this.departmentCode = departmentCode;
		this.parentCode = parentCode;
		this.childCode = childCode;
		this.childName = childName;
		this.rankNumber = rankNumber;
		this.isDeleted = isDeleted;
		this.locationIdx = locationIdx;
		this.regId = regId;
		this.regDate = regDate;
		this.modId = modId;
		this.modDate = modDate;
	}

	// 쿼리 전용 생성자 추가 (departmentName 포함)
	public DepartmentInfoDTO(String departmentCode, String departmentName, String childName, String parentCode,
			String childCode, String locationIdx) {
		this.departmentCode = departmentCode;
		this.departmentName = departmentName;
		this.childName = childName;
		this.parentCode = parentCode;
		this.childCode = childCode;
		this.locationIdx = locationIdx;
	}

	public DepartmentInfo toEntity() {
		return DepartmentInfo.builder().departmentCode(departmentCode).parentCode(parentCode).childCode(childCode)
				.childName(childName).rankNumber(rankNumber).isDeleted(isDeleted).locationIdx(locationIdx).regId(regId)
				.regDate(regDate).modId(modId).modDate(modDate).build();
	}

	public static DepartmentInfoDTO fromEntity(DepartmentInfo entity) {
		DepartmentInfoDTO dto = new DepartmentInfoDTO();
		dto.setIdx(entity.getIdx());
		dto.setDepartmentCode(entity.getDepartmentCode());
		dto.setParentCode(entity.getParentCode());
		dto.setChildCode(entity.getChildCode());
		dto.setChildName(entity.getChildName());
		dto.setRankNumber(entity.getRankNumber());
		dto.setIsDeleted(entity.getIsDeleted());
		dto.setLocationIdx(entity.getLocationIdx());
		dto.setRegId(entity.getRegId());
		dto.setRegDate(entity.getRegDate());
		dto.setModId(entity.getModId());
		dto.setModDate(entity.getModDate());
		return dto;
	}
}
