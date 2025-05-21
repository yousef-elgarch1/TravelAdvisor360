package com.example.traveladvisor360.interfaces;

import com.example.traveladvisor360.models.Experience;

/**
 * Interface for handling user interactions with experiences
 */
public interface ExperienceInteractionListener {

    /**
     * Called when an experience card is clicked
     * @param experience The clicked experience
     */
    void onExperienceClick(Experience experience);

    /**
     * Called when the like button is clicked
     * @param experience The experience being liked/unliked
     */
    void onLikeClick(Experience experience);

    /**
     * Called when the comment button is clicked
     * @param experience The experience to view/add comments for
     */
    void onCommentClick(Experience experience);

    /**
     * Called when the share button is clicked
     * @param experience The experience being shared
     */
    void onShareClick(Experience experience);

    /**
     * Called when the bookmark/save button is clicked
     * @param experience The experience being bookmarked
     */
    void onBookmarkClick(Experience experience);

    /**
     * Called when the book now button is clicked
     * @param experience The experience being booked
     */
    void onBookNowClick(Experience experience);
}