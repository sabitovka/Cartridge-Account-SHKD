CREATE TABLE [settings](
  [_id_set] INTEGER PRIMARY KEY ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL UNIQUE, 
  [cartridge] INTEGER NOT NULL REFERENCES [cartridge]([_id_c]) ON DELETE CASCADE ON UPDATE CASCADE, 
  [where_was_set] TEXT, 
  [install_date] DATE, 
  [uninstall_date] DATE);
