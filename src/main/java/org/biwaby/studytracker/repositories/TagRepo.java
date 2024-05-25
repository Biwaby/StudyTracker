package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Tag;
import org.biwaby.studytracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepo extends JpaRepository<Tag, Long> {

    List<Tag> findAllByUser(User user);
    void deleteAllByUser(User user);
}
