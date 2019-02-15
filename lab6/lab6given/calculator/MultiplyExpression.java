class MultiplyExpression extends BinaryExpression {

    public MultiplyExpression(final Expression lft, final Expression rht, final String operator) {
        super(lft, rht, operator);
    }

    protected double _applyOperator(double lft, double rht) {
        return lft * rht;
    }
}

