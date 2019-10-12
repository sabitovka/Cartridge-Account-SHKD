UPDATE cartridge SET 
       uuid = 'b3537a5c-a1b0-40c3-812f-f35a1d6f3bfa',
       producer = (SELECT _id FROM producer WHERE title LIKE 'CANON'), 
       name = 'Cartridge 703', 
       full_name = 'CANON Cartridge 703', 
       num = '1', 
       state = (Select _id_s FROM states WHERE title LIKE 'Установлен'), 
       note = '' 
WHERE uuid = 'b3537a5c-a1b0-40c3-812f-f35a1d6f3bfa'