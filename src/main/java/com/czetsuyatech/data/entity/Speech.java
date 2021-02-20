package com.czetsuyatech.data.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @version 0.0.1
 * @since 0.0.1
 */
@Entity
@Table(name = "speech", uniqueConstraints = @UniqueConstraint(columnNames = {"author", "date_of_speech"}))
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString(callSuper = true)
public class Speech extends BaseEntity {

	@NotNull
	@Column(name = "author", length = 100, nullable = false)
	private String author;

	@NotNull
	@Column(name = "date_of_speech", nullable = false)
	private LocalDate dateOfSpeech;

	@Column(name = "text", columnDefinition = "TEXT")
	private String text;

	@Column(name = "keywords", length = 255)
	private String keywords;
}
