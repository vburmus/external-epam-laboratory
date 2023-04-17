package com.epam.esm.giftcertificate.model;

import com.epam.esm.tag.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private List<Tag> tags;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
