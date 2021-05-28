package com.yh20studio.springbootwebservice.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;


//단순히 인터페이스를 생성후, JpaRepository를 상속하면 기본적인 CRUD 메소드가 자동생성 됩니다.
public interface PostsRepository extends JpaRepository <Posts, Long> {

    //규모가 있는 프로젝트에서의 데이터 조회는 FK의 조인, 복잡한 조건등으로 인해 이런 Entity 클래스만으로 처리하기 어려워 조회용 프레임워크를 추가로 사용합니다.
    //대표적 예로 querydsl, jooq, MyBatis 등이 있습니다.
    @Query("SELECT p " +
            "FROM Posts p " +
            "ORDER BY p.id DESC")
    Stream<Posts> findAllDesc();
}
