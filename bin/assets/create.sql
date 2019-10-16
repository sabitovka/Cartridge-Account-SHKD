CREATE TABLE [cartridge](
  [_id_c] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
  [uuid] CHAR(40), 
  [producer] INT REFERENCES [producer]([_id]), 
  [name] CHAR(40), 
  [full_name] CHAR(70), 
  [num] CHAR(50), 
  [state] INT(1) REFERENCES [states]([_id_s]) ON DELETE SET NULL ON UPDATE CASCADE, 
  [note] TEXT);
  
CREATE TABLE [producer](
  [_id] INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL ON CONFLICT FAIL UNIQUE, 
  [title] CHAR(20) NOT NULL);

CREATE TABLE [refuelings](
  [_id_r] INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL, 
  [cartridge] INTEGER NOT NULL ON CONFLICT FAIL REFERENCES [cartridge]([_id_c]) ON DELETE CASCADE ON UPDATE CASCADE, 
  [departure_date] DATE, 
  [arrival_date] DATE);

CREATE TABLE [settings](
  [_id_set] INTEGER PRIMARY KEY ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL UNIQUE, 
  [cartridge] INTEGER NOT NULL REFERENCES [cartridge]([_id_c]) ON DELETE CASCADE ON UPDATE CASCADE, 
  [where_was_set] TEXT, 
  [install_date] DATE, 
  [uninstall_date] DATE);

CREATE TABLE [states](
  [_id_s] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
  [title] CHAR(20) NOT NULL);
