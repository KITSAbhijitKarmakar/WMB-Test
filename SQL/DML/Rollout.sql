----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	Rollout.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Panchanan Mandal
--   Date	12/07/2013	--
--
--
----------------------------------------------------------------------------------------------------------------

insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('WIM129','Y', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('BQB458','Y', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('COK899','Y', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('DVE743','Y', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('DCT644','Y', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('TGT109','Y', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('JEY162','Y', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('MLK108','Y', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('RED175','N', sysdate);
insert into ROLLOUT (store_code, ready_for_rollout, rollout_date) values ('NMD190','Y', sysdate);

Commit;
