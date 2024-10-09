import React from 'react';

const PayPalButton = () => {
	return (<form action="https://www.paypal.com/donate" method="post" target="_blank">
			<input type="hidden" name="business" value="6Q4DHE47SKTE4"/>
			<input type="hidden" name="no_recurring" value="0"/>
			<input type="hidden" name="item_name" value="Thank you very much for your support of JarHC!"/>
			<input type="hidden" name="currency_code" value="CHF"/>
			<input type="image" src="/img/paypal-button.png" width="120" name="submit" title="PayPal" alt="Donate with PayPal button"/>
		</form>);
};

export default PayPalButton;
