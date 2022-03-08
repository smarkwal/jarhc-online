import React from 'react';

const PayPalButton = () => {
	return (<form action="https://www.paypal.com/donate" method="post" target="_blank">
			<input type="hidden" name="business" value="6Q4DHE47SKTE4" />
			<input type="hidden" name="no_recurring" value="0" />
			<input type="hidden" name="item_name" value="Thank you very much for your support of JarHC!" />
			<input type="hidden" name="currency_code" value="CHF" />
			<input type="image" src="https://pics.paypal.com/00/s/MWU3NjQyMzAtMmY4NC00NjMwLTg3MTAtZGZmMmYxZTE4MDk3/file.PNG" width="120" border="0" name="submit" title="PayPal - The safer, easier way to pay online!" alt="Donate with PayPal button" />
			<img alt="" border="0" src="https://www.paypal.com/en_CH/i/scr/pixel.gif" width="1" height="1" />
		</form>
	);
};

export default PayPalButton;
