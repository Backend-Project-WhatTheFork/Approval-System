package com.whatthefork.communicationandalarm.post.infrastructure.post;

import com.whatthefork.communicationandalarm.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post,Long> {
}
