----------------------------------------------------------------------------------------------------------------
--   SQL Create Script	DiscountType.sql
--   SQL Create Script : Schema MBREPOS.  
--
--   Created By	Vineet More
--   Date	19/06/2013	--
--
--
----------------------------------------------------------------------------------------------------------------
create table MBREPOS.GLRULES (
ACCTYPE                 VARCHAR2(255),
BALVALUTRULE            VARCHAR2(255),
VALUTRULE               VARCHAR2(255),
GLBAL                   VARCHAR2(255),
GLJEY                   VARCHAR2(255),
GLGEY                   VARCHAR2(255),
PRCTRRULE               VARCHAR2(255),
GLMIDL                  VARCHAR2(255),
KOSTLRULE               VARCHAR2(255),
GLDESC                  VARCHAR2(255),
GLRBS                   VARCHAR2(255),
BALGLDESC               VARCHAR2(255),
GLTYPE                  VARCHAR2(255),
BALZUONRRULE            VARCHAR2(255),
ZUONRRULE               VARCHAR2(255),
GLAIB                   VARCHAR2(255),
GLULST                  VARCHAR2(255))
TABLESPACE "MBODS" NOCOMPRESS LOGGING;
