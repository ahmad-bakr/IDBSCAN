
number_of_objects =   [0.200, 0.500, 1.000, 2.000, 3.000, 5.000, 7.000, 9.000, 12.000, 15.000, 18.000, 21.000, 25.000, 30.000, 34.000];

classic_time = [0.024, 0.083, 0.148, 0.194, 0.386, 1.089, 3.198, 8.986, 17.529, 23.826, 30.114, 39.833, 53.646, 70.375, 88.942];

enhanced_time = [0.014, 0.033, 0.071, 0.094, 0.104, 0.579, 1.943, 6.238, 14.257, 17.918, 23.745, 30.117, 43.574, 64.251, 77.202];

plot(number_of_objects, classic_time,'-.*k','LineWidth',1.4);
hold on
plot(number_of_objects, enhanced_time,'-+k','LineWidth',1.4);

leg = legend('Incremental DBSCAN','Enhanced Incremental DBSCAN');
set(leg,'Location','SouthEast');
hold off

xlabel('Number of Objects [Thousands]');
ylabel('Time [Sec]');
