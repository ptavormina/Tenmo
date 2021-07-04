TRUNCATE users, accounts, transfer_types, transfer_statuses, transfers CASCADE;


INSERT INTO users (user_id, username, password_hash) VALUES
(1000, 'Test1', 'test1')
(1001, 'Test2', 'test2')
(1002, 'Test3', 'test3');

INSERT INTO accounts (account_id, user_id, balance) VALUES
(2000, 1000, 1000),
(2001, 1001, 5000),
(2002, 1002, 500);


INSERT INTO transfer_types(transfer_type_id, transfer_type_desc) VALUES
(1, 'Request'),
(2, 'Send');

INSERT INTO transfer_statuses(transfer_status_id, transfer_status_desc) VALUES
(1, 'Pending'),
(2, 'Approved'),
(3, 'Rejected');

INSERT INTO transfers(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES
(3000 , 2, 1, 2000, 2001, 30),
(3001 , 2, 2, 2001, 2002, 60),
(3002 , 1, 1, 2002, 2000, 100);


