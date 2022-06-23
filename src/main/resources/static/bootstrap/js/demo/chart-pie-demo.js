
function fn_PieChart(data){

    var map_keys = new Array();
    var map_values = new Array();
    var cnt = 0;
    html_text  = '<div class="chart-pie pt-4 pb-2">';
    html_text += '<canvas id="myPieChart"></canvas>';
    html_text += '</div>';
    html_text += '<div class="mt-4 text-center small">' ;
    html_text += '<span class="mr-2">';

    for(let key of data.keys()){
        map_keys.push(key)
        map_values.push(data.get(key));
        if (cnt == 0 ){
            html_text += '<span class = "mr-2" > <i class = "fas fa-circle text-primary" > </i>';
        }else if (cnt == 1){
            html_text += '<span class = "mr-2" > <i class = "fas fa-circle text-success" > </i>';
        }else
            html_text += '<span class = "mr-2" > <i class = "fas fa-circle text-info" > </i>';

        html_text += key;
        html_text += '</span>';

        if(cnt == 2){
            break;
        }
        cnt++;
    }
    html_text += '</div>';
    html_text += '</div>';

    $('.chart-body').html(html_text);
    // Set new default font family and font color to mimic Bootstrap's default styling
    Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
    Chart.defaults.global.defaultFontColor = '#858796';

    // Pie Chart Example
    var ctx = document.getElementById("myPieChart");
    var myPieChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: map_keys,
            datasets: [{
                data: map_values,
                backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
                hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }],
        },
        options: {
            maintainAspectRatio: false,
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                caretPadding: 10,
            },
            legend: {
                display: false
            },
            cutoutPercentage: 80,
        },
    });
};