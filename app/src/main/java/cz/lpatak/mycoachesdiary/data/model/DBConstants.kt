package cz.lpatak.mycoachesdiary.data.model

class DBConstants {
    companion object {
        //COLLECTIONS
        const val TeamsCOLLECTION = "Teams"
        const val TrainingsCOLLECTION = "Trainings"
        const val ExercisesCOLLECTION = "Exercises"
        const val MatchesCOLLECTION = "Matches"

        //COLUMNS
        const val COLUMN_NAME = "name"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_TIME = "time"
        const val COLUMN_OWNER = "owner"
        const val COLUMN_SEASON = "season"
        const val COLUMN_PLACE = "place"
        const val COLUMN_RATING = "rating"
        const val COLUMN_DATE = "date"
        const val COLUMN_START_TIME = "startTime"
        const val COLUMN_END_TIME = "endTime"
        const val COLUMN_PLAYERS = "players"
        const val COLUMN_GOALKEEPERS = "goalkeepers"
        const val COLUMN_IMAGE_URL = "imageUrl"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TEAM = "team"
        const val COLUMN_OPPONENT = "opponent"
        const val COLUMN_TYPE = "type"
        const val COLUMN_PLAYING_TIME = "playingTime"
        const val COLUMN_NOTE = "note"
        const val COLUMN_SCORE_TEAM = "scoreTeam"
        const val COLUMN_SCORE_OPPONENT = "scoreOpponent"
        const val COLUMN_POWER_PLAYS_TEAM = "powerPlaysTeam"
        const val COLUMN_POWER_PLAYS_OPPONENT = "powerPlaysOpponent"
        const val COLUMN_POWER_PLAYS_TEAM_SUCESS = "powerPlaysTeamSuccess"
        const val COLUMN_POWER_PLAYS_OPPONENT_SUCESS = "powerPlaysOpponentSuccess"
        const val COLUMN_SHOTS_TEAM = "shotsTeam"
        const val COLUMN_SHOTS_OPPONENT = "shotsOpponent"
        const val COLUMN_SHOTS_TO_BLOCK = "shotsToBlock"
        const val COLUMN_SHOTS_OUTSIDE = "shotsOutside"

        // PREFERENCE MANAGER KEYS
        const val TEAM_ID_KEY = "TeamID"
        const val TEAM_NAME_KEY = "TeamName"
        const val TEAM_SEASON_KEY = "TeamSeason"
        const val TRAINING_ID_KEY = "TrainingID"
    }

}