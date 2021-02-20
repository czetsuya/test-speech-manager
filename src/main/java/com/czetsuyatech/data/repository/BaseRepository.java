package com.czetsuyatech.data.repository;

import com.czetsuyatech.data.entity.BaseEntity;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, I extends Serializable> extends JpaRepository<T, I> {

}
