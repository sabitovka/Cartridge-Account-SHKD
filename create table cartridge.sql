CREATE TABLE [cartridge](
  [_id_c] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
  [uuid] CHAR(40), 
  [producer] INT REFERENCES [producer]([_id]), 
  [name] CHAR(40), 
  [full_name] CHAR(70), 
  [num] CHAR(50), 
  [state] INT(1) REFERENCES [states]([_id_s]) ON DELETE SET NULL ON UPDATE CASCADE, 
  [note] TEXT);