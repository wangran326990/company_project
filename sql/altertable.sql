ALTER TABLE `account_tran`
    -- Add new indexes
    ADD INDEX `IDX_ACCOUNT_TRAN_DATETIME` (`DATETIME`),
    ADD INDEX `IDX_ACCOUNT_TRAN_GAME_ID` (`GAME_ID`),

    -- Replace composite indexes with single-column indexes
    DROP INDEX `IDX_ACCOUNT_TRAN_2`,
    ADD INDEX `IDX_ACCOUNT_TRAN_2` (`GAME_TRAN_ID`),

    DROP INDEX `IDX_ACCOUNT_TRAN_3`,
    ADD INDEX `IDX_ACCOUNT_TRAN_3` (`PLATFORM_TRAN_ID`);