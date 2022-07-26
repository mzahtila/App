package com.mzahtila.thesisapp.Data;

import android.provider.BaseColumns;

public class GroupContract implements BaseColumns {
    /**
     * Name of database table
     */
    public final static String TABLE_NAME = "results";

    /**
     * Unique ID number for the group (only for use in the database table).
     * <p>
     * Type: INTEGER
     */
    public final static String _ID = BaseColumns._ID;

    /**
     * Group number - Unique ID number for the group
     * <p>
     * Type: TEXT
     */
    public final static String GR_NUM = "GROUP_NUMBER";

    /**
     * Group name
     * <p>
     * Type: TEXT
     */
    public final static String GR_NAM = "GROUP_NAME";

    /**
     * Group members.
     * <p>
     * Type: TEXT
     */
    public final static String GR_MEM = "GROUP_MEMBERS";

    /**
     * Score of the group.
     * <p>
     * Type: INTEGER
     */
    public final static String SCORE = "GROUP_SCORED";

    /**
     * Total possible score in the quiz.
     * <p>
     * Type: INTEGER
     */
    public final static String MAX_SCORE = "MAX_QUIZ_SCORE";

    /**
     * Status of lecture: 0 - not completed, n- completed in n-th try
     * <p>
     * Type: TEXT
     */
    public final static String PLD_CHK1 = "PLAYED_CHK1";

    /**
     * Status of lecture: 0 - not completed, n- completed in n-th try
     * <p>
     * Type: TEXT
     */
    public final static String PLD_CHK2 = "PLAYED_CHK2";

    /**
     * Status of lecture: 0 - not completed, n- completed in n-th try
     * <p>
     * Type: TEXT
     */
    public final static String PLD_CHK3 = "PLAYED_CHK3";

    /**
     * Status of lecture: 0 - not completed, n- completed in n-th try
     * <p>
     * Type: TEXT
     */
    public final static String PLD_CHK4 = "PLAYED_CHK4";

    /**
     * Answer to comprehension question of lecture (checkpoint) 1
     * <p>
     * Type: TEXT
     */
    public final static String ANSW_CHK1 = "ANSWER_CHK1";

    /**
     * Answer to comprehension question of lecture (checkpoint) 2
     * <p>
     * Type: TEXT
     */
    public final static String ANSW_CHK2 = "ANSWER_CHK2";

    /**
     * Answer to comprehension question of lecture (checkpoint) 3
     * <p>
     * Type: TEXT
     */
    public final static String ANSW_CHK3 = "ANSWER_CHK3";

    /**
     * Answer to comprehension question of lecture (checkpoint) 4
     * <p>
     * Type: TEXT
     */
    public final static String ANSW_CHK4 = "ANSWER_CHK4";
}
