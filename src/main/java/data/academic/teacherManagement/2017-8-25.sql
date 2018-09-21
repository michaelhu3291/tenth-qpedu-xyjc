ALTER TABLE tbl_ref_Exam_Class_Course
ADD Exam_Zf int NULL ,
Exam_Yx int NULL ,
Exam_Lh int NULL ,
Exam_Jg int


DROP TABLE [dbo].[tbl_ScoreForEveyStuCourse]
GO
CREATE TABLE [dbo].[tbl_ScoreForEveyStuCourse] (
[Id] varchar(38) NOT NULL ,
[Name] varchar(100) NULL ,
[TotalScore] decimal(10) NOT NULL ,
[Create_Time] datetime NOT NULL ,
[Create_Person] varchar(38) NOT NULL ,
[School_Year] varchar(20) NOT NULL ,
[School_Type] varchar(20) NOT NULL ,
[Term] varchar(20) NOT NULL ,
[Exam_Type] varchar(20) NOT NULL ,
[Course] varchar(20) NOT NULL ,
[Exam_Number] varchar(38) NOT NULL ,
[Class_Name] varchar(20) NOT NULL ,
[Class_Type] varchar(20) NOT NULL ,
[School_Code] varchar(38) NOT NULL ,
[Class_Id] varchar(38) NOT NULL ,
[Class_Grade] varchar(100) NULL ,
[Score_List] varchar(255) NULL ,
[School_Name] varchar(38) NOT NULL ,
[XJH] varchar(30) NOT NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'tbl_ScoreForEveyStuCourse', 
'COLUMN', N'TotalScore')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'总分'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'TotalScore'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'总分'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'TotalScore'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'tbl_ScoreForEveyStuCourse', 
'COLUMN', N'School_Year')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'学年'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'School_Year'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'学年'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'School_Year'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'tbl_ScoreForEveyStuCourse', 
'COLUMN', N'School_Type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'学校类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'School_Type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'学校类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'School_Type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'tbl_ScoreForEveyStuCourse', 
'COLUMN', N'Term')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'学期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'Term'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'学期'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'Term'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'tbl_ScoreForEveyStuCourse', 
'COLUMN', N'Exam_Type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'考试类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'Exam_Type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'考试类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'Exam_Type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'tbl_ScoreForEveyStuCourse', 
'COLUMN', N'Course')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'科目'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'Course'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'科目'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'tbl_ScoreForEveyStuCourse'
, @level2type = 'COLUMN', @level2name = N'Course'
GO

-- ----------------------------
-- Indexes structure for table tbl_ScoreForEveyStuCourse
-- ----------------------------
CREATE INDEX [分数] ON [dbo].[tbl_ScoreForEveyStuCourse]
([TotalScore] ASC) 
GO
CREATE INDEX [学校] ON [dbo].[tbl_ScoreForEveyStuCourse]
([School_Code] ASC) 
GO
CREATE INDEX [课程] ON [dbo].[tbl_ScoreForEveyStuCourse]
([Course] ASC) 
GO
