----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	MbSequence.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------
create table MBREPOS.MBSEQUENCE (
SEQUENCENAME            VARCHAR(50),
MAXVALUE                VARCHAR(50),
NEXTVALUE               VARCHAR(50))
TABLESPACE "MBODS" NOCOMPRESS LOGGING; 
