yBar=[0.214 0.268 0.298 ;
      0.211 0.268 0.299
      ]
bar(yBar);
%title('Time taken to calculate similarties to clusters for CIG and DIG');
leg = legend('T5-8K', 'T4-8K', 'T7-10K');
set(gca,'XTickLabel',{'Incremental Enhanced DBSCAN','Incremental DBSCAN'},  'FontSize', 8) 
set(leg,'Location','NorthEast');

ylabel('Davies Bouldin Index');
