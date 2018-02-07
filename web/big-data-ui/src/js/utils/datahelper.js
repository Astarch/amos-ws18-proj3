function normalizeData(graphdata) {
    //console.time("normalizeData")
    let max = Object.values(graphdata).reduce((max, current) => {
        return current > max ? current : max
    }, -1)
    let normalizeFactor = 1 / (max / 100)
    let normalized = Object.keys(graphdata).reduce((previous, current) => {
        previous[current] = Math.round(graphdata[current] * normalizeFactor)
        return previous;
    }, {});
    //console.timeEnd("normalizeData")
    return normalized
}

function normalizeDataArray(dataArray) {
    let normalizedArray = dataArray.map(value => {
        return Object.assign({}, value, { data: normalizeData(value.data) })
    })
    return normalizedArray
}

function median(values) {
    values = values.slice(0).sort(function(a, b) { return a - b; });

    return middle(values);
}

function middle(values) {
    var len = values.length;
    var half = Math.floor(len / 2);

    if (len % 2)
        return (values[half - 1] + values[half]) / 2.0;
    else
        return values[half];
}

function calculateMinMaxAvgMedianOfData(graphdata) {
    const values = Object.values(graphdata)
    let min = parseInt(Math.min(...values), 10);
    let max = parseInt(Math.max(...values), 10);
    let sum = 0;
    for (var i = 0; i < values.length; i++) {
        sum += parseInt(values[i], 10); //don't forget to add the base
    }
    let avg = parseInt(sum / values.length, 10);
    let _median = parseInt(median(values), 10);

    return Object.assign({}, {
        min,
        max,
        avg,
        median: _median
    })
}

function aggregateDataArray(dataArray) {
    let additionalDataArray = dataArray.map(value => {
        return Object.assign({}, value, calculateMinMaxAvgMedianOfData(value.data))
    })
    return additionalDataArray
}



export {
    normalizeDataArray,
    aggregateDataArray
}