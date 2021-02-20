package com.czetsuyatech.data.repository;

import com.czetsuyatech.data.entity.Speech;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@Repository
public class CustomSpeechRepositoryImpl implements CustomSpeechRepository {

	private final EntityManager em;

	public CustomSpeechRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	private String likeNeedle(String needle) {
		return "%" + needle.toUpperCase() + "%";
	}

	@Override
	public List<Speech> findByMetadata(String author, String keyword, String text, LocalDate from, LocalDate to) {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Speech> criteriaQuery = criteriaBuilder.createQuery(Speech.class);
		Root<Speech> root = criteriaQuery.from(Speech.class);

		List<Predicate> predicates = new ArrayList<>();
		if (!ObjectUtils.isEmpty(author)) {
			Expression<String> path = root.get("author");
			Expression<String> upperExpr = criteriaBuilder.upper(path);
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(upperExpr, likeNeedle(author))));
		}

		if (!ObjectUtils.isEmpty(keyword)) {
			Expression<String> path = root.get("keywords");
			Expression<String> upperExpr = criteriaBuilder.upper(path);
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(upperExpr, likeNeedle(keyword))));
		}

		if (!ObjectUtils.isEmpty(text)) {
			Expression<String> path = root.get("text");
			Expression<String> upperExpr = criteriaBuilder.upper(path);
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(upperExpr, likeNeedle(text))));
		}

		if (!ObjectUtils.isEmpty(from)) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfSpeech").as(LocalDate.class), from));
		}

		if (!ObjectUtils.isEmpty(to)) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateOfSpeech").as(LocalDate.class), to));
		}

		Predicate wherePredicates = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		criteriaQuery.where(wherePredicates);
		Query query = em.createQuery(criteriaQuery);

		return query.getResultList();
	}
}
