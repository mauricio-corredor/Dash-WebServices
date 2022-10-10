package com.dash.infra.repository

import com.dash.domain.model.workoutwidget.WorkoutExercise
import com.dash.domain.model.workoutwidget.WorkoutExerciseId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WorkoutExerciseRepository : JpaRepository<WorkoutExercise, WorkoutExerciseId> {

    @Query("SELECT * FROM workout_exercise WHERE workout_session_id = :workoutSessionId", nativeQuery = true)
    fun findAllByWorkoutSessionId(workoutSessionId: Int): List<WorkoutExercise>
}