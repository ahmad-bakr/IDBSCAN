yBar=[0.73 0.84 0.631 ;
      0.72 0.83 0.627
      ]
bar(yBar);
%title('Time taken to calculate similarties to clusters for CIG and DIG');
leg = legend('T5-8K', 'T4-8K', 'T7-10K');
set(gca,'XTickLabel',{'Incremental Enhanced DBSCAN','Incremental DBSCAN'},  'FontSize', 8) 
set(leg,'Location','NorthEast');

ylabel('Dunn Index');
