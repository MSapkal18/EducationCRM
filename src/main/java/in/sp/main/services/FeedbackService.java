package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Feedback;
import in.sp.main.repositories.FeedbackRepository;

@Service
public class FeedbackService 
{
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	public void sendFeedback(Feedback feedback)
	{
		feedbackRepository.save(feedback);
	}
	
	public Page<Feedback> getAllFeedbacksByPagination(Pageable pageable)
	{
		return feedbackRepository.findAll(pageable);
	}
	
	public boolean updateFeedbackStatus(Long id, String status)
	{
        // Find the feedback by its ID
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        
        // If feedback exists, update its status
        if (feedback != null) 
        {
            feedback.setReadStatus(status);
            feedbackRepository.save(feedback);
            return true; // Return true if the update was successful
        }
        
        // Return false if feedback with the given ID was not found
        return false;
    }
}
