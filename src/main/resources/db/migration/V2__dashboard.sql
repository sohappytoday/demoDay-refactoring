WITH RECURSIVE dates AS (
    SELECT DATE('2025-09-01') AS d
    UNION ALL
    SELECT DATE_ADD(d, INTERVAL 1 DAY)
    FROM dates
    WHERE d < '2026-01-31'
)
INSERT INTO statistics (
    statistic_daily_visited,
    statistic_daily_reserved,
    statistic_daily_deposited,
    statistic_daily_sales,
    statistic_deposit_expired,
    statistic_deposit_24hour,
    statistic_date
)
SELECT
    0 AS statistic_daily_visited,
    0 AS statistic_daily_reserved,
    0 AS statistic_daily_deposited,
    0 AS statistic_daily_sales,
    0 AS statistic_deposit_expired,
    0 AS statistic_deposit_24hour,
    d AS statistic_date
FROM dates
ON DUPLICATE KEY UPDATE
                     statistic_daily_visited     = VALUES(statistic_daily_visited),
                     statistic_daily_reserved    = VALUES(statistic_daily_reserved),
                     statistic_daily_deposited   = VALUES(statistic_daily_deposited),
                     statistic_daily_sales       = VALUES(statistic_daily_sales),
                     statistic_deposit_expired   = VALUES(statistic_deposit_expired),
                     statistic_deposit_24hour    = VALUES(statistic_deposit_24hour);