package com.whatthefork.communicationandalarm.post.infrastructure.post;

import com.whatthefork.communicationandalarm.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post,Long> {

    Optional<Post> findByIdAndIsDeletedFalse(Long postId);

}
