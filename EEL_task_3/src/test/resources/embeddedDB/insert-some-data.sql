INSERT INTO gift_certificate
VALUES ( 1,'GiftCertificate1', 'description', 20, 20, '2023-01-15 00:36:20',
        '2023-01-16 01:20:09');
INSERT INTO gift_certificate
VALUES ( 2,'GiftCertificate2', 'description', 20, 20, '2023-01-15 00:36:55',
        '2023-01-16 01:25:34');

INSERT INTO tag
VALUES (1,'Tag1');
INSERT INTO tag
VALUES (2,'Tag2');

INSERT INTO gift_certificate_has_tag
VALUES (1, 1);
INSERT INTO gift_certificate_has_tag
VALUES (1, 2);
INSERT INTO gift_certificate_has_tag
VALUES (2, 2);

INSERT INTO user
VALUES (1,'FIRST','FIRST',112);
INSERT INTO user
VALUES (2,'SECOND','SECOND',122);

INSERT INTO purchase
VALUES (1,1,1,0,'...', '2023-01-15 00:36:20',
        '2023-01-16 01:20:09');
INSERT INTO purchase
VALUES (2,2,12,0,'...', '2023-01-15 00:36:20',
        '2023-01-16 01:20:09');


INSERT INTO gift_certificate_has_order
VALUES(1,1,2);

INSERT INTO gift_certificate_has_order
VALUES(2,2,3);