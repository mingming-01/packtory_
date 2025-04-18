package kr.co.itwillbs.de.mes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.DecimalMin;
import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@RequiredSessionIds(fields = { "regId", "modId" })
public class ProductDTO {
    private Long idx;  				// 인덱스 (PK)
    
    @NotBlank(message = "타입은 필수 입력 값입니다.")
    private String type;  			// 타입
    
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;  			// 이름
    
    @NotNull(message = "개수는 필수 입력 값입니다.")
    private Integer quantity;  		// 개수
    
    @NotBlank(message = "단위는 필수 입력 값입니다.")
    private String unit;  			// 단위
    
    @PositiveOrZero(message = "가격은 0원 이상만 입력 가능합니다.")
    private BigDecimal price;  		// 가격
    
    @Pattern(regexp = "^[YN]$", message = "삭제여부는 'Y' 또는 'N'이어야 합니다.")
    private String isDeleted;  		// 삭제여부
    
    private String regId;  			// 최초등록자 ID
    private LocalDateTime regDate;  // 최초등록일
    private String modId;  			// 최종수정자 ID
    private LocalDateTime modDate;  // 최종수정일
    
    // 한글 처리
    private String typeName;
}
