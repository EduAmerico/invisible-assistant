package com.assistant.invisible_assistant.repository;

import com.assistant.invisible_assistant.model.GPTResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GPTResponseRepository extends JpaRepository<GPTResponse, Long> { }
