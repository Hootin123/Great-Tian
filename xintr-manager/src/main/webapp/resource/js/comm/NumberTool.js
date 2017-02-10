/**
 * 将数字千分位显示
 * @param num
 * @returns {string}
 */
function currency(num) {
    return (num.toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
}