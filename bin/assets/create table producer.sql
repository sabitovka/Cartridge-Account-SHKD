CREATE TABLE [producer](
  [_id] INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL ON CONFLICT FAIL UNIQUE, 
  [title] CHAR(20) NOT NULL);
