SET GLOBAL log_bin_trust_function_creators = 1;

DELIMITER $$
CREATE FUNCTION test_data()
    RETURNS BIGINT

BEGIN

    declare phone_num_seed BIGINT default 4000009000;
    declare step INT default 1000000;
    while step > 0
        Do
            select cast(phone_num_seed as char(50)) into @phone_number;
            select left(@phone_number, 6) into @phone_number_temp;
            select left(@phone_number_temp, 3) into @num1;
            select right(@phone_number_temp, 3) into @num2;
            select right(@phone_number, 4) into @num3;

            select if(step mod 2 = 0, 'Home', 'Cell') into @phone_type;

            INSERT INTO doordash.phone_record (phone_number, phone_type, occurrences)
            VALUES (concat(@num1, '-', @num2, '-', @num3), @phone_type, 1);

            set phone_num_seed = phone_num_seed - 1;
            set step = step - 1;
        end while;
    return phone_num_seed;
END

select test_data();