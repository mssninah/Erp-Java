package s6.eval.first.crm.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    private Long id;
    
    @JsonProperty("external_id")
    private String externalId;
    
    private String title;
    private String description;
    
    @JsonProperty("status_id")
    private Long statusId;
    
    @JsonProperty("user_assigned_id")
    private Long userAssignedId;
    
    @JsonProperty("user_created_id")
    private Long userCreatedId;
    
    @JsonProperty("client_id")
    private Long clientId;
    
    @JsonProperty("project_id")
    private Long projectId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private LocalDateTime deadline;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    
}
