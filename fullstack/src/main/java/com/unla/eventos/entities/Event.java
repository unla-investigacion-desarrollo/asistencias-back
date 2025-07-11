package com.unla.eventos.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.unla.eventos.helpers.FunctionsHelper;
import com.unla.eventos.helpers.PublicLinksHelper;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startDate;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime endDate;

    private String uniqueCode;
    
    private String mailContact;
    
    private String imagePath;
    
    private String title;
    
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public String startDateFormatted() {
    	return FunctionsHelper.formatLocalDateToARGTime(getStartDate());
    }
    
    public String endDateFormatted() {
    	return FunctionsHelper.formatLocalDateToARGTime(getEndDate());
    }
    
    public String publicLink() {
    	return PublicLinksHelper.PUBLIC_REGISTER_LINK + getUniqueCode();
    }
    
    public String publicResumeLink() {
    	return PublicLinksHelper.PUBLIC_REGISTER_RESUME_LINK + getUniqueCode();
    }
    
    public String responsesLink() {
    	return PublicLinksHelper.PUBLIC_EVENT_RESPONSES + getId();
    }
}
