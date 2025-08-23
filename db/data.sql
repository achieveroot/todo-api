INSERT INTO `todos` (`public_id`, `title`, `completed`, `priority`, `deadline`) VALUES
(UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-','')), '할일 1', 0, 'NONE', NULL),
(UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-','')), '할일 2, 1', 'LOW',  NULL),
(UNHEX(REPLACE('33333333-3333-3333-3333-333333333333','-','')), '할일 3',  0, 'HIGH', NULL);