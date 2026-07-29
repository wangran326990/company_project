SELECT
          ID,
          ACCOUNT_ID,
          DATETIME,
          TRAN_TYPE,
          PLATFORM_TRAN_ID,
          GAME_TRAN_ID,
          GAME_ID,
     
          (
      	    COALESCE(AMOUNT_REAL, 0)
      	    + COALESCE(AMOUNT_RELEASED_BONUS, 0)
      	    + COALESCE(AMOUNT_PLAYABLE_BONUS, 0)
      	    + COALESCE(AMOUNT_UNDERFLOW, 0)
      	    + COALESCE(AMOUNT_FREE_BET, 0)
      	) AS amount,
     
          (
              COALESCE(BALANCE_REAL, 0)
              + COALESCE(BALANCE_RELEASED_BONUS, 0)
              + COALESCE(BALANCE_PLAYABLE_BONUS, 0)
          ) AS balance
      FROM account_tran WHERE ID = 71;

# check if game is played on multiple platforms
SELECT
    GAME_ID,
    COUNT(DISTINCT PLATFORM_ID) AS platform_count
FROM account_tran
GROUP BY GAME_ID
HAVING COUNT(DISTINCT PLATFORM_ID) > 1;
