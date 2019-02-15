class AddExpression extends BinaryExpression {

    public AddExpression(final Expression lft, final Expression rht, final String operator) {
        super(lft, rht, operator);
    }

    protected double _applyOperator(double lft, double rht) {
        return lft + rht;
    }
}
